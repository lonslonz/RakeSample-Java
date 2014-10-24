package com.skplanet.rakesample;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.pdp.sentinel.shuttle.RakeSampleSentinelShuttle;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final String TOKEN = "9e9b36b3ec8bdce6de5d4b591b7fb6ff731d04e";
	
    public static void main( String[] args )
    {
        RakeSampleSentinelShuttle shuttle = new RakeSampleSentinelShuttle();
        
        SimpleDateFormat formmater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        
        shuttle.setBase_time(formmater.format(new Date()));
        shuttle.setLocal_time(formmater.format(new Date()));
        shuttle.rake_lib_version("r0.5.0_c1.3").rake_lib("android");
        shuttle.setToken(TOKEN);
        shuttle.page_id("myPage").action_id("MyAction").action_extra_value("extra what");
        
        try {
        	sendLogsToRake(shuttle);
        } catch(Exception e) {
        	e.printStackTrace();
        	return;
        }
        System.out.println("Success");
    }
    
    private static void sendLogsToRake(RakeSampleSentinelShuttle shuttle) throws Exception{
    	ObjectMapper mapper = new ObjectMapper();
    	
    	String content = convertShuttleToRakeJson(shuttle);
    	System.out.println(content);
    	
    	String logToSend = 
    	HttpUtil.sendHttpPut(
    			//"http://localhost:8000/log/trackJson",
    			"https://pg.rake.skplanet.com:8443/log/trackJson",
    			content, 
    			2000, 
    			2000);
    }
    private static String convertShuttleToRakeJson(RakeSampleSentinelShuttle shuttle) throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
    	
    	RakeJsonLog rakeJsonLog = new RakeJsonLog();
    	rakeJsonLog.setCompress("plain");
    	Map<String, Object> shuttleMap = mapper.readValue(shuttle.toJSONString(), Map.class);
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	Map<String, Object> propertiesMap = new HashMap<String, Object>();
    	
    	for(Map.Entry<String, Object> entry : shuttleMap.entrySet()) {
    		if(entry.getKey().equals("sentinel_meta")) {
    			Map<String, Object> shuttleMetaMap = (Map<String, Object>)entry.getValue();
    			for(Map.Entry<String, Object> entry2 : shuttleMetaMap.entrySet()) {
    				dataMap.put(entry2.getKey(), entry2.getValue());
    			}	
    		} else {
    			propertiesMap.put(entry.getKey(), entry.getValue());
    		}
    	}
    	dataMap.put("properties", propertiesMap);
    	
    	List<Map<String, Object>> shuttleMapList = new LinkedList<Map<String, Object>>();
    	shuttleMapList.add(dataMap); 
    	
    	rakeJsonLog.setData(shuttleMapList);
    	return mapper.writeValueAsString(rakeJsonLog);
    }
}

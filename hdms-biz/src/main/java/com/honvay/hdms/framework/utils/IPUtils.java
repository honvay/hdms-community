package com.honvay.hdms.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class IPUtils {

	private static final String IP_QUERY_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&&ip=";
	
	public static Position getPosition(String ip) {
		StringBuffer sb = new StringBuffer();
		URLConnection urlcon;
		try {
			urlcon = new URL(IP_QUERY_URL + ip).openConnection();
			InputStreamReader isr = new InputStreamReader(
					urlcon.getInputStream(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			ObjectMapper mapper = new ObjectMapper();
			Position position = mapper.readValue(sb.toString(), Position.class);
			//JSONObject result = JSONObject.fromObject(sb.toString());
			/*if (!result.getString("ret").equals("-1")) {
				Position position = (Position) JSONObject.toBean(result,
						Position.class);
				return position;
			}*/
			if(position != null && position.getCountry() != null){
				return position;
			}
			return null;
		} catch (Exception e1) {
			return null;
		}
	}
	
	public static String getLongCity(String ip){
		try {
			Position position = getPosition(ip);
			return position.getCountry() + position.getProvince() + position.getCity();
		} catch (Exception e) {
			return "";
		}
	}

	public static String getCity(String ip) {
		try {
			return getPosition(ip).getCity();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static void main(String[] args) {
		System.out.println(IPUtils.getPosition("219.137.52.167"));
	}
}

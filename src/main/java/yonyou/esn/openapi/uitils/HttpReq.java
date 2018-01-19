package yonyou.esn.openapi.uitils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class HttpReq {
	private final static Logger Log = LoggerFactory.getLogger(HttpReq.class);
	/**
	 * 发送 get 请求
	 * @method sendGet
	 * @param url
	 * @param key
	 * @param value
	 * @return String
	 * @throws ConnectException 
	 * @throws SocketTimeoutException 
	 * @date 2015年3月25日 下午6:43:54
	 */
	public static String sendGet(String url, String key, String value) 
			throws SocketTimeoutException, ConnectException {
		Map<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		return sendGet(url, map);
	}

	/**
	 * 发送 get 请求
	 * @method sendGet
	 * @param url
	 * @param map
	 * @return string
	 * @date 2015年3月25日 下午6:42:02
	 */
	public static String sendGet(String url, Map<String, String> map) 
			throws SocketTimeoutException, ConnectException {
		String param = null;
		try {
			param = getParams2(map);
		} catch (UnsupportedEncodingException e1) {
			Log.error(e1.getMessage(),e1);
		}
		String result = "";
		BufferedReader in = null;
		try {

			String urlName = url + "?" + param;

			URL realUrl = new URL(urlName);
			
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", 
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			//定义BufferedReader输入流来读取URL的响应
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine())!=null) {
				result += line;
			}
			
		} catch (Exception e) {
			Log.error(e.getMessage(),e);
			throw new ConnectException("ConnectException Url : "+url);
		} finally {
			//使用finally块来关闭输入流
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException ex) {
				Log.error(ex.getMessage(),ex);
			}			
		}
		return result;
	}
	
	/**
	 * 发送 post 请求
	 * @method sendPost
	 * @param url
	 * @param key
	 * @param value
	 * @return String
	 * @throws SocketTimeoutException
	 * @throws ConnectException
	 * @date 2015年3月25日 下午5:00:55
	 */
	public static String sendPost(String url, String key, String value) 
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		return sendPost(url, map);
	}
	
	/**
	 * 发送 post 请求 
	 * @method sendPost
	 * @param url
	 * @param map
	 * @return String
	 * @throws Exception
	 * @date 2015年3月25日 下午4:48:02
	 */
	public static String sendPost(String url, Map<String, String> map)
			throws Exception {
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setConnectTimeout(TIMING_OUT);
			conn.setReadTimeout(TIMING_OUT);
			// 获取URLConnection对象对应的输出流

			out = conn.getOutputStream();
			outw = new OutputStreamWriter(out, "UTF-8");
			// 发送请求参数
			 System.out.println(getParams(map));
			outw.write(getParams(map));
			// flush输出流的缓冲
			outw.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));

			String line;
			String result = "";
			while ((line = in.readLine()) != null) {
				result += line;
			}
			return result;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e2) {
				Log.error(e2.getMessage(), e2);
			}
		}
	}
	
	public static String postBody(String urlString, String json) {
		if (urlString.startsWith("https:")) {
			return postBodyHttps(urlString, json); 
		}
		return postBodyHttp(urlString, json);
	}
	
	private static String postBodyHttp(String urlString, String json) {
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedReader in = null; 
		try {
			URL url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// 设置通用的请求属性
			urlConnection.setRequestProperty("accept", "*/*");
			urlConnection.setRequestProperty("connection", "Keep-Alive");
			urlConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			urlConnection.setRequestProperty("content-type", "application/json");

			// 发送POST请求必须设置如下两行
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);

			urlConnection.setConnectTimeout(TIMING_OUT);
			urlConnection.setReadTimeout(TIMING_OUT);
			
			out = urlConnection.getOutputStream();
			outw = new OutputStreamWriter(out, "UTF-8");
			
			outw.write(json);
			outw.flush();
			outw.close();
			//定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            
            String line;
            String result = "";
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
        	return result;
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                if (in != null) {
                    in.close();
                }
                if(out!=null){
                	out.close();
                }
            } catch (Exception e2) {
               Log.error(e2.getMessage(),e2);
            }
        }
		return null;
	}
	
	private static String postBodyHttps(String urlString, String json) {
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedReader in = null; 
		try {
			URL url = new URL(urlString);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			// 设置通用的请求属性
			urlConnection.setRequestProperty("accept", "*/*");
			urlConnection.setRequestProperty("connection", "Keep-Alive");
			urlConnection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			urlConnection.setRequestProperty("content-type", "application/json");

			// 发送POST请求必须设置如下两行
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);

			urlConnection.setConnectTimeout(TIMING_OUT);
			urlConnection.setReadTimeout(TIMING_OUT);
			
			out = urlConnection.getOutputStream();
			outw = new OutputStreamWriter(out, "UTF-8");
			
			outw.write(json);
			outw.flush();
			outw.close();
			//定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            
            String line;
            String result = "";
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
        	return result;
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                if (in != null) {
                    in.close();
                }
                if(out!=null){
                	out.close();
                }
            } catch (Exception e2) {
               Log.error(e2.getMessage(),e2);
            }
        }
		return null;
	
	}
	/**
	 * Map 解析为 String
	 * @method getParams
	 * @param map
	 * @return String
	 * @throws UnsupportedEncodingException 
	 * @date 2015年3月25日 下午4:49:56
	 */
	private static String getParams2(Map<String, String> map) throws UnsupportedEncodingException{
		if(map.size()==0)
			return "";
		StringBuilder s = new StringBuilder();
		for(String key : map.keySet()){
			s.append("&").append(key).append("=").append(null != map.get(key)?URLEncoder.encode(map.get(key),"UTF-8"):"");
		}
		return s.toString().replaceFirst("&", "");
	}
	/**
	 * Map 解析为 String
	 * @method getParams
	 * @param map
	 * @return String
	 * @throws UnsupportedEncodingException 
	 * @date 2015年3月25日 下午4:49:56
	 */
	private static String getParams(Map<String, String> map) {
		if(map.size()==0)
			return "";
		StringBuilder s = new StringBuilder();
		for(String key : map.keySet()){
			s.append("&").append(key).append("=").append(map.get(key));
		}
		return s.toString().replaceFirst("&", "");
	}

	private static int TIMING_OUT = 4000;
}

  

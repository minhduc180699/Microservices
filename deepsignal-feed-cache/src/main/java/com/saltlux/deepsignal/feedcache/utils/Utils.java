package com.saltlux.deepsignal.feedcache.utils;


import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.springframework.util.ObjectUtils.isArray;

public class Utils {
	/////////////////////////////////////////////////////////////////////////////////////////////////
	private static Logger logger = LoggerFactory.getLogger(Utils.class);
	//const
	public static final String STRING_EMPTY = "";
	public static final String STRING_NULL = "NULL";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//개발 환경일 때 설정되는 변수
	//주의) Storm Cluster에 배포되었을 때는 Topology에서 설정한 값이 전달되지 않으므로 기본 설정을 false로 해두어야 한다
	private static boolean debugMode=false;
	public static boolean bIsDebug() { return debugMode; };
	public static void setDebug(boolean on) {
		debugMode=on;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Gson 객체는 Thread Safe하다.
	public static final Gson  gson;
	static {
		gson=new Gson();
	}
	
    ////////////////////////////////////////////////////////////////////////////////////////////////
	public static String getStackTrace(Throwable e) 
	{
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static long getMurmurHash(String key) 
    {
		ByteBuffer bfKey;
		try {
			bfKey = ByteBuffer.wrap(key.getBytes("UTF-8"));
	        if (bfKey.remaining() == 0) return Long.MIN_VALUE;

	        long[] hash = new long[2];
	        MurmurHash.hash3_x64_128(bfKey, bfKey.position(), bfKey.remaining(), 0, hash);
	        return hash[0] == Long.MIN_VALUE ? Long.MAX_VALUE : hash[0];
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Long.MIN_VALUE; // Never called here!
    }
    //KAY_ADD_END	

    public static String getMurmurHashString(String key) 
    {
    	final long hash=getMurmurHash(key);
    	if ( hash>=0 ) return Long.toString(hash);
    	return 'N'+Long.toString(-1*hash);
    }
    
    /**
     * MySQL의 MD5 함수와 동일한 값이 생성됨
     * 
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(String key) {
		try {
	    	MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(key.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) { //java에 "MD5"가 없는 경우는 없다.
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String a = getMD5("https://console.cloud.google.com/storage/browser/artifacts.bright-lattce-339306.appspot.com/realtimedata?project=bright-lattice-339306&pageState=(%22StorageObjectListTable%22:(%22f%22:%22%255B%255D%22))&prefix=&forceOnObjectsSortingFiltering=false");
		String b = getMD5("https://console.cloud.google.com/storage/browser/artifacts.bright-lattice-339306.appspot.com/realtimedata?project=bright-lattice-339306&pageState=(%22StorageObjectListTable%22:(%22f%22:%22%255B%255D%22))&prefix=&forceOnObjectsSortingFiltering=false");
		String c = getMD5("https://consoage/browser/artifacts.bright-lattice-339306.appspot.com/realtimedata?project=bright-lattice-339306&pageState=(%22StorageObjectListTable%22:(%22f%22:%22%255B%255D%22))&prefix=&forceOnObjectsSortingFiltering=false");
		String d = "dasd";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
	public static boolean isEmpty(Object array[]){
		if ( array==null ) return true;
		if ( array.length==0 ) return true;
		return false;
	}
    
	public static boolean isNotEmpty(Object array[]){
		if ( array==null ) return false;
		if ( array.length==0 ) return false;
		return true;
	}

	public static boolean isEmpty(@SuppressWarnings("rawtypes") Collection c){
		if ( c==null ) return true;
		if ( c.size()==0 ) return true;
		return false;
	}
	
	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Collection c){
		if ( c==null ) return false;
		if ( c.size()==0 ) return false;
		return true;
	}
	
	public static boolean isEmpty(@SuppressWarnings("rawtypes") Map m){
		if ( m==null ) return true;
		if ( m.size()==0 ) return true;
		return false;
	}
	
	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Map m){
		if ( m==null ) return false;
		if ( m.size()==0 ) return false;
		return true;
	}
	
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean equals(String str1, String str2) {
        if (str1 != null ) {
        	if ( str2==null ) return false;
        	return str1.equals(str2);
        }
        if (str2 != null ) return str2.equals(str1);
        return true;
    }

    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String[]> loadFromResource(String resourcePath) throws IOException {
		URL url = Resources.getResource(resourcePath);
		//String text = Resources.toString(url, Charsets.UTF_8);
		
		List<String[]> list=new ArrayList<String[]>();
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				line=line.trim();
				if ( line.length()<=0 || line.startsWith("#") || line.startsWith("/") ) continue;

				String[] s = line.split("\\s+");	// 공백, 탭 등의 문자가 하나 이상 있을 시에 몽창 묶어서 split
				list.add(s);
			}
		} finally {
			if ( in!=null ) in.close();
		}
		return list;
	}
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static String readFile(final File file, final String encoding) throws FileNotFoundException, IOException {
		try(FileInputStream fin = new FileInputStream(file) ) {
			final byte fileContent[] = new byte[(int)file.length()];
			fin.read(fileContent);
			return new String(fileContent, encoding);
		}
	}
	
	public static StringBuilder append(StringBuilder sb, char seperator, CharSequence text) {
		if ( text==null || text.length()<=0 ) return sb;
		if ( sb.length()>0 ) sb.append(seperator);
		sb.append(text);
		return sb;
	}
	
	public static String join(CharSequence seperator, CharSequence ... elements) {
		if ( elements==null || elements.length<=0 ) return "";
		StringBuilder sb=new StringBuilder();
		for(CharSequence text : elements) {
			if ( sb.length()>0 ) sb.append(seperator);
			if ( text!=null ) sb.append(text);
		}
		return sb.toString();
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//common-io에서 가져온 소스
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    private static final int NOT_FOUND = -1;

    private static void failIfNullBytePresent(final String path) {
        final int len = path.length();
        for (int i = 0; i < len; i++) {
            if (path.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no " +
                        "known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }    
    public static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static String getFileName(final String filename) {
        if (filename == null) {
            return null;
        }
        failIfNullBytePresent(filename);
        final int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }
	public static String generateDirectory(String url,String threadName) {
		String filePath = "";
		try {
			filePath = java.net.URLDecoder.decode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            logger.error("THREAD " + threadName + " --- ERROR DECODE URL FILEPATH : " + e.getMessage(), e);
            filePath = url;
        }
        filePath = filePath.replace("https://", "");
		filePath = filePath.replace("http://", "");
		filePath = filePath.replace("www.", "");
		filePath = filePath.replace("http://", "");
		filePath = filePath.substring(0, filePath.lastIndexOf("/"));
		filePath = filePath.replace("/", File.separator);
		filePath = filePath.replace("\\", File.separator);
		filePath = filePath.replace(".", File.separator);
		filePath = filePath.replace("?", File.separator);
		filePath = filePath.replace("\"", "");
		if (filePath.contains("BHPFD00000000001?action_type")) {
			filePath = filePath.substring(0, filePath.lastIndexOf("?action_type"));
		}
		if (filePath.contains("CMMUTIL010109A10?downloadDir")) {
			filePath = filePath.substring(0, filePath.lastIndexOf("?downloadDir"));
		}
		if (filePath.contains("CMMUTIL010109A10?downloadDir")) {
			filePath = filePath.substring(0, filePath.lastIndexOf("?downloadDir"));
		}
		return filePath;
	}


	public static long getHashCode(HashMap<String, Object> record, List<String> keyFields) {
		long hash = 0;

		for (String key : keyFields) {
			hash = hash(hash, record.get(key));
		}

		return hash;
	}

	public static long hash(long hash, Object object) {
		long result = hash;
		if (object == null) {
			result = hash(result, 0);
		} else if (!ObjectUtils.isArray(object)) {
			result = hash(result, object.hashCode());
		} else {
			for (Object item : (List<?>) object) {
				// if an item in the array references the array itself, prevent infinite looping
				if (!(item == object)) // recursive call!
				{
					result = hash(result, item);
				}
			}
		}

		return result;
	}

	public static long hash(long hash, int value) {
		return hash * 31 + value;
	}
}


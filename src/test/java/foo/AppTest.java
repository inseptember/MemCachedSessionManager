package foo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.genscript.cache.util.KryoObjectTransCoder;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	protected static MemCachedClient mcc = new MemCachedClient(true);
	
	static{
		String[] servers = {"10.168.2.141:11211"};
		Integer[] weights = {3};
		
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers(servers);
		pool.setWeights(weights);
		
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(5);
		pool.setMaxIdle(1000 * 60 * 60 * 6);
		
		pool.setMaintSleep(30);
		
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(0);
		
		pool.initialize();
		mcc.setTransCoder(new KryoObjectTransCoder());
	}
	
	public static void buildCache(){
		HashMap<String, List<Object>> p = new LinkedHashMap<String, List<Object>>();
		p.put("dd", new ArrayList<Object>());
		SysMsgs s = new SysMsgs();
		s.setContent("ccc");
		s.setFrom(23);
		s.setTt(p);
		
		List<SysMsgs> list = new ArrayList<SysMsgs>();
		list.add(s);
		mcc.set("test", list, new Date(10 * 1000));
	}
	
	public static void output(){
		List<SysMsgs> v = (List<SysMsgs>) mcc.get("test");
		System.out.println(v.size());
	}
	
	public static void main(String[] args) {
		buildCache();
		output();
	}

}

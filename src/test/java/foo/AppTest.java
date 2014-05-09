package foo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.genscript.cache.util.KryoObjectTransCoder;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;


/**
 * Unit test for simple App.
 */
public class AppTest 
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
	}
	
	public static void output(){
		Object v = (Object) mcc.get("9A2D846D7D3FF8CAC5894BAA30C93845OrderItemList");
		System.out.println(v);
	}
	
	public static void main(String[] args) {
		buildCache();
		output();
	}

}

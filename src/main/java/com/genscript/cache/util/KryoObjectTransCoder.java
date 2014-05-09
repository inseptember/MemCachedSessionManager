package com.genscript.cache.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.schooner.MemCached.AbstractTransCoder;

public class KryoObjectTransCoder extends AbstractTransCoder {
	
	Logger logger = LoggerFactory.getLogger(KryoObjectTransCoder.class);
	
	private static ThreadLocal<Kryo> serialThreadLocal = new ThreadLocal<Kryo>();
	
	public Object decode(InputStream in) throws IOException {
		Object o = null;
		Input input = null;
		try {
			input = new Input(in);
			o = getKryo().readClassAndObject(input);
		} catch (Exception e) {
			logger.error("kryo decode inputstream failed", e);
		}finally{
			if(input != null){
				input.close();
			}
		}
		return o;
	}
	
	public Kryo getKryo(){
		Kryo kryo = serialThreadLocal.get();
		if(kryo == null){
			kryo = new Kryo();
			serialThreadLocal.set(kryo);
		}
		return kryo;
	}

	@Override
	public void encode(OutputStream out, Object object) throws IOException {
		Output output = null;
		try {
			output = new Output(out);
			getKryo().writeClassAndObject(output, object);
		} catch (Exception e) {
			logger.error("kryo encode outputstream failed", e);
		}finally{
			if(output!=null){
				output.close();
			}
		}
		
	}
	
	public Object decode(InputStream in, ClassLoader classLoader) throws IOException{
		Object o = null;
		getKryo().setClassLoader(classLoader);
		Input input = null;
		try {
			input = new Input(in);
			o = getKryo().readClassAndObject(input);
		} catch (Exception e) {
			logger.error("kryo decode inputstream failed", e);
		}finally{
			if(input != null){
				input.close();
			}
		}
		return o;
	}

}

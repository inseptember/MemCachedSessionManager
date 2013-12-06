package com.genscript.cache.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.schooner.MemCached.AbstractTransCoder;
import com.whalin.MemCached.ContextObjectInputStream;

public class KryoObjectTransCoder extends AbstractTransCoder {
	
	private Kryo kryo;

	public Object decode(InputStream in) throws IOException {
		Object o = null;
		if(kryo == null){
			kryo = new Kryo();
		}
		Input input = new Input(in);
		o = kryo.readClassAndObject(input);
		input.close();
		return o;
	}

	@Override
	public void encode(OutputStream out, Object object) throws IOException {
		if(kryo == null){
			kryo = new Kryo();
		}
		Output output = new Output(out);
		kryo.writeClassAndObject(output, object);
		output.close();
	}
	
	public Object decode(InputStream in, ClassLoader classLoader) throws IOException{
		if(kryo == null){
			kryo = new Kryo();
		}
		Object o = null;
		kryo.setClassLoader(classLoader);
		Input input = new Input(in);
		o = kryo.readClassAndObject(input);
		input.close();
		return o;
	}

}

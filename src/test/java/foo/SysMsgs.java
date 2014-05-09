package foo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;



public class SysMsgs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7427309066182173422L;

	private Integer id;
	
	private Integer from;
	
	private String subject;
	
	private String content;
	
	private Integer refId;
	
	private HashMap<String, List<Object>> tt;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public void setTt(HashMap<String, List<Object>> tt) {
		this.tt = tt;
	}

	public HashMap<String, List<Object>> getTt() {
		return tt;
	}

	public void write(Kryo kryo, Output output) {
		kryo.writeObject(output, this);
	}

	public void read(Kryo kryo, Input input) {
		kryo.readObject(input, this.getClass());  
	}
	
	public static void main(String[] args) throws Exception {
		File f = new File("C:\\sys.bin");
		OutputStream out = new FileOutputStream(f);
		SysMsgs s = new SysMsgs();
		s.setContent("00000000000000000000111111111111111111111111000000000000000000000000000000000000000000000000111111111111");
		
		Kryo k = new Kryo();
		k.register(SysMsgs.class);
		Output output = new Output(out);
		k.writeClassAndObject(output, s);
		out.flush();
		output.close();
	}

}

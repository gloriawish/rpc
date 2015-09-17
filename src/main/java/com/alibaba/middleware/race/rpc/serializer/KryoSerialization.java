package com.alibaba.middleware.race.rpc.serializer;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
public class KryoSerialization {
	private Kryo kryo;
	private Registration registration = null;
	private Class<?> t;
	public KryoSerialization() {
		// TODO Auto-generated constructor stub
		kryo = new Kryo(); 
		kryo.setReferences(true); 
		// kryo.setRegistrationRequired(true); 
		// kryo.setInstantiatorStrategy(new StdInstantiatorStrategy()); 
	}

	public void register(Class<?> T) {
		//注册类 
		t = T;
		registration = kryo.register(t); 
	}
	public byte[] Serialize(Object object) {
		Output output = null; 
		// ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
		//output = new Output( outStream , 4096); 
		output = new Output(1, 4096); 
		//kryo.writeObject(output, object); 
		kryo.writeClassAndObject(output, object);
		byte[] bb = output.toBytes(); 
		// System.out.println(bb.length); 
		output.flush(); 

		return bb;
	}

	public <t> t Deserialize(byte[] bb) {
		Input input = null; 
		// input = new Input(new 
		// ByteArrayInputStream(outStream.toByteArray()),4096); 
		input = new Input(bb); 
		//		Student s = (Student) kryo.readObject(input, registration.getType()); 
		@SuppressWarnings("unchecked")
		t res = (t) kryo.readClassAndObject(input);
		input.close();
		return res;
	}
}

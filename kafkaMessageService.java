package com.sbic.turbine.iservices;

public interface kafkaMessageService {

	public void submitRequesttoKafka(Object requestObject, Object responseObject);
	
}

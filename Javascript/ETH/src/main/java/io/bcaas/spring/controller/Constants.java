package io.bcaas.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定義Log, method name
 * 
 * @since 2018/01/01
 * 
 * @author Costa Peng
 * 
 * @version 1.0.0
 * 
 */

public class Constants {

	// Log name for appender
	public static final String LOG_INFO = "log.info";
	public static final String LOG_DEBUG = "log.debug";

	public static final Logger LOGGER_INFO = LoggerFactory.getLogger(LOG_INFO);
	public static final Logger LOGGER_DEBUG = LoggerFactory.getLogger(LOG_DEBUG);


}

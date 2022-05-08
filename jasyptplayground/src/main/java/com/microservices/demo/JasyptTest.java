package com.microservices.demo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class JasyptTest {

  public static void main(String[] args) {

    StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
    standardPBEStringEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
    standardPBEStringEncryptor.setIvGenerator(new RandomIvGenerator());
    standardPBEStringEncryptor.setPassword("modernmicroservices@kafkamongospringboot");
    System.out.println(standardPBEStringEncryptor.encrypt("b6q0SQtFVFCnGrGcj2os5hGyAwRJ4ujsvPy5nvD5LC1Du"));

  }

}

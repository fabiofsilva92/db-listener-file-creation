//package com.itau.dblistenerfilecreation.application;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itau.dblistenerfilecreation.domain.entities.LogVeiculo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//@Service
//@Slf4j
//public class S3UploadFileService {
//
//    @Value("${aws.access-key-id}")
//    private String accessKeyId;
//
//    @Value("${aws.secret-access-key}")
//    private String secretAccessKey;
//
//    public void uploadFile(LogVeiculo logVeiculo, Object dadosVeiculo, ObjectMapper objectMapper) throws IOException {
//        log.info("Tentando fazer upload to S3");
//        try {
//            String bucketName = "db-change-files";
//            String key = "export_" + logVeiculo.getVeiculoId() +"_"+logVeiculo.getOperacao()+ "_" + System.currentTimeMillis() + ".json";
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            objectMapper.writeValue(baos, dadosVeiculo);
//            byte[] fileContentBytes = baos.toByteArray();
//
//            Region region = Region.SA_EAST_1;
//
//            S3Client s3 = S3Client.builder()
//                    .credentialsProvider(StaticCredentialsProvider.create(
//                            AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
//                    .region(Region.of(region.toString()))
//                    .build();
//
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(key)
//                    .build();
//
//            s3.putObject(putObjectRequest, RequestBody.fromBytes(fileContentBytes));
//            log.info("S3 incluido com sucesso.");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}

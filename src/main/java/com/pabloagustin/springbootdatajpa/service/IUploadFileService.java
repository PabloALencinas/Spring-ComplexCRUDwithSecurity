package com.pabloagustin.springbootdatajpa.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadFileService {

    // Metodos del contraro
    public Resource load(String filename) throws MalformedURLException;
    public String copy(MultipartFile file) throws IOException;
    public boolean delete(String filename);

    // Metodos para inicializar y eliminar el directorio uploads cada vez que iniciamos el proyecto
    public void deleteAll();
    public void init() throws IOException;
}

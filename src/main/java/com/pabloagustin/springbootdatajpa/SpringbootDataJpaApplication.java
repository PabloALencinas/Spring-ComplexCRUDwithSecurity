package com.pabloagustin.springbootdatajpa;

import com.pabloagustin.springbootdatajpa.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDataJpaApplication implements CommandLineRunner {

	@Autowired
	private MvcConfig mvcConfig;

	// Inyectamos la clase service para el manejo del INIT y DELETE del directorio uploads
	@Autowired
	IUploadFileService uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();

		String password = "12345";
		// Generamos dos password encryptadas a traves del encoder
		for (int i = 0; i < 2; i++){
			String bcryptPassword = mvcConfig.passwordEncoder().encode(password);
			System.out.println(bcryptPassword);
		}
	}
}

package gr.tei.erasmus.pp.eventmate.backend;


import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

public class Tst {

    @Test
    public void testStringFromFile(){

        Blob blob = FileUtils.getFileBlob(new File("aa.jpg"));

        System.out.println(FileUtils.getEncodedStringFromBlob(blob));

    }

    @Test
    public void testStream() throws IOException {

//        List<Obj> list = Arrays.asList(new Obj(1L),new Obj(2L));
//
//
//        List<Long> ids = list.stream().map(Obj::getId).collect(Collectors.toList());
//
//        System.out.println(ids);



        File f = new File("joke.jpg");


        byte[] encodedBytes = Base64.getEncoder().encode(Files.readAllBytes(f.toPath()));


        String imageString = new String(encodedBytes);


        // odeslani



        String recievedString = imageString;

        byte[] decodedBytes = Base64.getDecoder().decode(recievedString);



        try {
            Blob blob = new SerialBlob(decodedBytes);

            File outputFile = new File("new.jpg");
            FileOutputStream fout = new FileOutputStream(outputFile);
            IOUtils.copy(blob.getBinaryStream(), fout);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    class Obj{
        Long id;

        public Obj(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}

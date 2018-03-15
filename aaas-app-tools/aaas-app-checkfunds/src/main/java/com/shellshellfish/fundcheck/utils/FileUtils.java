package com.shellshellfish.fundcheck.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by chenwei on 2018- 三月 - 09
 */

public class FileUtils {
  public static void trimFile(String originFilePath, String targetFilePath)
      throws FileNotFoundException {
    FileReader inputStream = null;
//    FileWriter outputStream = null;

    try(FileWriter outputStream = new FileWriter(targetFilePath)) {
      inputStream = new FileReader(originFilePath);


      int c;
      while ((c = inputStream.read()) != -1) {
        outputStream.write(c);
      }
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {

    }


//    PrintWriter writer = new PrintWriter(targetFilePath);
////    try (Stream<String> stream = Files.lines(Paths.get(originFilePath), Charset.forName("UTF8"))) {
////
////      stream.forEach(line->{
////        if(!StringUtils.isEmpty(line) && !line.startsWith(",")){
////          writer.write(line);
////          writer.write("\n");
////        }
////      });
////
////    } catch (IOException e) {
////      e.printStackTrace();
////    }
//
//    CharsetDecoder dec= StandardCharsets.ISO_8859_1.newDecoder()
//        .onMalformedInput(CodingErrorAction.IGNORE);
//    Path path=Paths.get(originFilePath);
//    List<String> lines;
//    try(Reader r= Channels.newReader(FileChannel.open(path), dec, -1);
//        BufferedReader br=new BufferedReader(r)) {
//        br.lines().forEach(
//              line->{
//                if(!StringUtils.isEmpty(line) && !line.startsWith(",")){
//                  writer.write(line);
//                  writer.write("\n");
//                }
//              }
//          );
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    writer.close();
//
//    long length = 0;
//    RandomAccessFile f = new RandomAccessFile(targetFilePath, "rw");
//
//    try{
//      byte b;
//      length = f.length();
//      do {
//        length -= 1;
//        f.seek(length);
//         b = f.readByte();
//      } while(b != '\n');
//      f.setLength(length);
//      f.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

  }


}

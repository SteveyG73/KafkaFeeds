/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafkafeeder;

import org.apache.logging.log4j.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import com.mpatric.mp3agic.*;
import javax.json.*;
/**
 *
 * @author Steve
 */
public class MP3List {

    
    private static final Logger LOGKF = LogManager.getLogger("kafkafeeder");
    private List<JsonObject> mp3Json;
    /**
     * @param fpath
     */
    public MP3List(String fpath) {
        
        mp3Json = new ArrayList(); 
        
        try (Stream<Path> paths = Files.walk(Paths.get(fpath))){
            paths.forEach((f) -> {
               String fname = f.toString();
               if (fname.endsWith("mp3") ) {
                  LOGKF.debug("Adding "+fname);
                  try {
                       Mp3File mp3 = new Mp3File(fname);
                       if (mp3.hasId3v2Tag()) {
                           ID3v2 tags = mp3.getId3v2Tag();
                           JsonObject json = Json.createObjectBuilder()
                                .add("filename", fname)
                                .add("title", tags.getTitle())
                                .add("artist", tags.getArtist())
                                .add("album", tags.getAlbum())
                                .add("genre", tags.getGenreDescription())
                                .add("track", tags.getTrack())
                                .add("year", tags.getYear())
                                .build();
                           mp3Json.add(json);
                           LOGKF.debug(json.toString());
                       }
                  }
                  catch (Exception e) {
                      LOGKF.error(e);
                  }
               }
            });
        }
        catch (Exception e) {
            LOGKF.error(e);
        }
    }
    
    public List<JsonObject> getMP3List() {
        return mp3Json;
    }
}

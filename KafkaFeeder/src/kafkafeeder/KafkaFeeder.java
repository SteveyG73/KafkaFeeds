/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafkafeeder;
import org.apache.logging.log4j.*;
import java.util.ArrayList;
import java.util.List;
import javax.json.*;
import java.util.Properties;
import org.apache.kafka.clients.producer.*;
/**
 *
 * @author Steve
 */
public class KafkaFeeder {

    /**
     * @param args the command line arguments
     */
    
    private static final Logger LOG = LogManager.getLogger("kafkafeeder");
    
    public static void main(String[] args) {
        // TODO code application logic here

        String kafkaTopic;
        int i = 0;
        Properties props = new Properties();
               
        if (args.length!=1) {
            System.out.println("Incorrect number of parameters supplied...");
            LOG.error("Incorrect number of parameters supplied...");
            System.exit(1);
        }
        kafkaTopic = args[0];        
        LOG.info("Setting Kafka topic to "+kafkaTopic);
        props.put("bootstrap.servers", "192.168.1.92:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        LOG.info("Connecting to "+kafkaTopic);
        Producer<String, String> kafka = new KafkaProducer<>(props);
        LOG.info("Connection successful");
        
        LOG.info("Building file list...");
        MP3List mp3s = new MP3List("D:\\Music");
        
        LOG.info("Sending files...");
        while (i < mp3s.getMP3List().size()) {

          String payload = mp3s.getMP3List().get(i).toString();
          i++;
          kafka.send(new ProducerRecord<String, String>(kafkaTopic, Integer.toString(i), payload ));
        }
        
        LOG.info("Closing Connectiion to "+kafkaTopic);        
        kafka.close();
        LOG.info("Connection successfully dropped");
        
    }
    
}

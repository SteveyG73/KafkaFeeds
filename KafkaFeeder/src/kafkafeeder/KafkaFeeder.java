/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafkafeeder;
import org.apache.logging.log4j.*;
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
        props.put("retries", 3);
        props.put("linger.ms", 1000);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        LOG.info("Connecting to "+kafkaTopic);
        Producer<String, String> kafka = new KafkaProducer<String,String>(props);
        LOG.info("Connection successful");
        
        LOG.info("Building file list...");
        MP3List mp3s = new MP3List("D:\\Music\\30 Seconds to Mars");
        
        LOG.info("Sending "+mp3s.getMP3List().size()+" files...");
        while (i < mp3s.getMP3List().size()) {

          String payload = mp3s.getMP3List().get(i).toString();
          i++;
          LOG.info(payload);
          kafka.send(new ProducerRecord<String, String>(kafkaTopic, Integer.toString(i), payload ));
        }
        
        LOG.info("Closing Connectiion to "+kafkaTopic);        
        kafka.close();
        LOG.info("Connection successfully dropped");
        
    }
    
}

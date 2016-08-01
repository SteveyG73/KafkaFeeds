/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafkafeeder;
import org.apache.logging.log4j.*;
/**
 *
 * @author Steve
 */
public class KafkaFeeder {

    /**
     * @param args the command line arguments
     */
    
    private static final Logger LOG = LogManager.getLogger();
    
    public static void main(String[] args) {
        // TODO code application logic here
        String kafkaTopic;
        
        if (args.length!=1) {
            System.out.println("Incorrect number of parameters supplied...");
            System.exit(1);
        }
        kafkaTopic = args[0];        
        LOG.info("Setting Kafka topic to "+kafkaTopic);
    }
    
}

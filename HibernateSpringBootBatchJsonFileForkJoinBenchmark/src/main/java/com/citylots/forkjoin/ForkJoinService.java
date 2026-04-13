package com.citylots.forkjoin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ForkJoinService {

    private final ApplicationContext applicationContext;

    public ForkJoinService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public static final int NUMBER_OF_LINES_TO_INSERT = 1000; // 10000, and 25000
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();; // 4, and Runtime.getRuntime().availableProcessors();
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_CORES);

    private static final Logger logger = Logger.getLogger(ForkJoinService.class.getName());

    public List<String> fileToDatabase(String fileName) throws IOException {

        logger.info("Reading file lines into a List<String> ...");

        // fetch 200000+ lines
        List<String> allLines = Files.readAllLines(Path.of(fileName)); 

        // run on a snapshot of NUMBER_OF_LINES_TO_INSERT lines
        List<String> lines = allLines.subList(0, NUMBER_OF_LINES_TO_INSERT);

        logger.info(() -> "Read a total of " + allLines.size()
                + " lines, inserting " + NUMBER_OF_LINES_TO_INSERT + " lines");
        
        return lines;        
    }

    public void forkjoin(List<String> lines) {
        
        logger.info(() -> "Preparing to insert " + lines.size() + " lots ...");
        ForkingComponent forkingComponent
                = applicationContext.getBean(ForkingComponent.class, lines);
        forkJoinPool.invoke(forkingComponent);
    }
}

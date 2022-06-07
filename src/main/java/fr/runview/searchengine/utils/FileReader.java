package fr.runview.searchengine.utils;

import com.opencsv.CSVReader;
import fr.runview.searchengine.domain.Cat;
import fr.runview.searchengine.domain.Gender;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class FileReader {
    public List<Cat> readCatsCSV(MultipartFile file) throws Exception {
        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8), ';' , '"');
        List<String[]> list = csvReader.readAll();
        csvReader.close();
        return convert(list);
    }

    private List<Cat> convert(List<String[]> entries) {
        return entries.stream().skip(1).map(FileReader::convert).collect(Collectors.toList());
    }

    private Cat convert(String[] entry) {
        final Cat cat = new Cat();
        cat.setNom(entry[0]);
        cat.setGender("femelle".equalsIgnoreCase(entry[1]) ? Gender.FEMELLE : Gender.MALE);
        cat.setBirthDay(LocalDate.parse(entry[2], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        cat.setRace(entry[3]);
        cat.setPrice(Double.valueOf(entry[4].replace(',', '.')));
        cat.setComment(entry[5]);
        return cat;
    }
}

package fr.runview.searchengine.services;

import fr.runview.searchengine.domain.Cat;
import fr.runview.searchengine.domain.Gender;
import fr.runview.searchengine.repositories.CatRepository;
import fr.runview.searchengine.utils.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class CatService {
    private final CatRepository catRepository;

    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public List<Cat> uploadCSV(MultipartFile file) throws Exception {
        List<Cat> cats = FileReader.readCatsCSV(file);
        catRepository.saveAll(cats);
        log.info("upload {} cats from csv file {} success", cats.size(), file.getOriginalFilename());
        return cats;
    }

    public Page<Cat> findAll(Integer page) {
        log.info("find all cats page {}", page);
        PageRequest request = PageRequest.of(page, 5);
        return catRepository.findAll(request);
    }

    public List<Cat> findAllFemales() {
        log.info("find all females cats");
        return catRepository.findChatByGender(Gender.FEMELLE);
    }

    public List<Cat> findAllMales() {
        log.info("find all mates cats");
        return catRepository.findChatByGender(Gender.MALE);
    }

    public List<Cat> filterByPrice(Double eq, Double sup, Double inf, Double[] between) {
        if (!ObjectUtils.isEmpty(eq)) {
            log.info("filter cats by price equal {}", eq);
            return catRepository.findChatByPrice(eq);
        }

        if (!ObjectUtils.isEmpty(sup)) {
            log.info("filter cats by price greater than {}", sup);
            return catRepository.filterCatsByPriceGreaterThan(sup);
        }

        if (!ObjectUtils.isEmpty(inf)) {
            log.info("filter cats by price less than {}", inf);
            return catRepository.filterCatsByPriceLessThan(inf);
        }

        if (!ObjectUtils.isEmpty(between) && between.length == 2) {
            log.info("filter cats by prices between {} and {}", between[0], between[1]);
            return catRepository.filterCatsByPriceBetween(between[0], between[1]);
        }

        throw new IllegalArgumentException("unsupported price filter");
    }

    public List<Cat> filterByAge(Integer eq, Integer sup, Integer inf, Integer[] between) {
        if (!ObjectUtils.isEmpty(eq)) {
            LocalDate min = LocalDate.now().minusYears(eq + 1).plusDays(1);
            LocalDate max = LocalDate.now().minusYears(eq);
            log.info("filter cats by age eq {}, birth day between {} and {}", eq, min, max);
            return catRepository.filterCatsByBirthDayBetween(min, max);
        }

        if (!ObjectUtils.isEmpty(sup)) {
            LocalDate min = LocalDate.now().minusYears(sup + 2);
            log.info("filter cats by age greater than {}, birth day <= {}", sup, min);
            return catRepository.filterCatsByBirthDayBefore(min);
        }

        if (!ObjectUtils.isEmpty(inf)) {
            LocalDate max = LocalDate.now().minusYears(inf + 1).plusDays(1);
            log.info("filter cats by age less than {}, birth day >= {}", inf, max);
            return catRepository.filterCatsByBirthDayAfter(max);
        }

        if (!ObjectUtils.isEmpty(between) && between.length == 2) {
            LocalDate refMax = LocalDate.now().minusYears(between[0]);
            LocalDate refMin = LocalDate.now().minusYears(between[1]+1).plusDays(1);
            log.info("filter cats by age between {} and {}, birth day between {} and {}", between[0], between[1], refMin, refMax);
            return catRepository.filterCatsByBirthDayBetween(refMin, refMax);
        }
        throw new IllegalArgumentException("unsupported age filter");
    }

    public Cat save(Cat cat) {
        if (ObjectUtils.isEmpty(cat)) {
            throw new IllegalArgumentException("new cat object cannot be null");
        }

        if (!ObjectUtils.isEmpty(cat.getId())) {
            throw new IllegalArgumentException("new cat object cannot have defined id");
        }
        log.info("add new cat to database {}", cat);
        return catRepository.save(cat);
    }

    public Cat update(Cat cat) {
        if (ObjectUtils.isEmpty(cat)) {
            throw new IllegalArgumentException("given cat object cannot be null");
        }

        if (ObjectUtils.isEmpty(cat.getId()) || !catRepository.existsById(cat.getId())) {
            throw new IllegalArgumentException("cannot find cat with given Id");
        }
        log.info("update cat {}", cat.getId());
        return catRepository.save(cat);
    }

    public void delete(Long id) {
        if (!catRepository.existsById(id))
            throw new IllegalArgumentException("cannot find cat with given Id");

        log.info("delete cat {}", id);
        catRepository.deleteById(id);
    }
}

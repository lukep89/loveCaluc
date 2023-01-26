package sg.edu.nus.iss.app.loveCalcu.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sg.edu.nus.iss.app.loveCalcu.model.LoveResult;

@Service
public class LoveCalcuService {
    private static final String LOVE_CALCU_API_URL = "https://love-calculator.p.rapidapi.com/getPercentage";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public Optional<LoveResult> getResult(LoveResult rr) throws IOException {

        String finalLoveCalcuUrl = UriComponentsBuilder
                .fromUriString(LOVE_CALCU_API_URL)
                .queryParam("fname", rr.getFname())
                .queryParam("sname", rr.getSname())
                .toUriString();
        System.out.println("finalUrl >>> " + finalLoveCalcuUrl);

        String loveApiKey = System.getenv("LOVE_API_KEY");
        String loveApiHost = System.getenv("LOVE_API_HOST");

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", loveApiKey);
        headers.set("X-RapidAPI-Host", loveApiHost);

        RequestEntity<Void> req = RequestEntity
                .get(finalLoveCalcuUrl)
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers)
                .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);
        // System.out.println("response >>> " + resp);

        LoveResult w = LoveResult.create(resp.getBody());
        // System.out.println("loveResult model >>> " + w);

        if (w != null) {
            redisTemplate.opsForValue().set(w.getId(), resp.getBody().toString());

            return Optional.of(w);
        }
        return Optional.empty();

    }

    public LoveResult[] allResultList() throws IOException {
        // use redis command to get all keys. will return a Set
        Set<String> allKeys = redisTemplate.keys("*");

        // create a List to store all the LoveResult models
        List<LoveResult> resultList = new LinkedList<>();
        for (String key : allKeys) {
            String result = (String) redisTemplate.opsForValue().get(key);

            resultList.add(LoveResult.create(result));
        }

        // return new arr
        return resultList.toArray(new LoveResult[resultList.size()]);
    }

    public Optional<LoveResult> getById(String id) throws IOException {
        String result = (String) redisTemplate.opsForValue().get(id);

        if (result == null) {
            return Optional.empty();
        }

        LoveResult lrObj = LoveResult.create(result);
        return Optional.of(lrObj);
    }
}

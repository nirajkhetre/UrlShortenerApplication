package in.avaloneco.UrlShortenerApplication.impl;

import in.avaloneco.UrlShortenerApplication.config.Base62Encoder;
import in.avaloneco.UrlShortenerApplication.dto.UrlMappingDto;
import in.avaloneco.UrlShortenerApplication.entity.UrlMapping;
import in.avaloneco.UrlShortenerApplication.repository.UrlMappingRepository;
import in.avaloneco.UrlShortenerApplication.service.UrlShortenerService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ModelMapper modelMapper;

    public UrlShortenerServiceImpl(
            UrlMappingRepository urlMappingRepository,
            RedisTemplate<Object, Object> redisTemplate,
            ModelMapper modelMapper) {
        this.urlMappingRepository = urlMappingRepository;
        this.redisTemplate = redisTemplate;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public String shortenUrl(String originalUrl) {

        //validate input url
        if (originalUrl == null || originalUrl.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        try {
            new java.net.URL(originalUrl);
        } catch (java.net.MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + originalUrl);
        }


        //save to db/get auto increment
        UrlMappingDto urlMapping = new UrlMappingDto();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setCreatedAt(Instant.now());
        UrlMapping urlMapping1 =urlMappingRepository.save(modelMapper.map(urlMapping, UrlMapping.class));

        //Base62 encode id to get shortcode
        String shortCode = Base62Encoder.encode(urlMapping1.getId());

        //save shortCode back to entity
        urlMapping1.setShortCode(shortCode);
        urlMappingRepository.save(urlMapping1);

        redisTemplate.opsForValue().set(shortCode, originalUrl, 24, java.util.concurrent.TimeUnit.HOURS);


        return shortCode;
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        //try redis
        Object cached = redisTemplate.opsForValue().get(shortCode);
        if (cached != null) {
            return cached.toString();
        }

        //fallback to db
        Optional<UrlMapping> mapping = urlMappingRepository.findByShortCode(shortCode);

        if (mapping.isPresent()){
            //cahe againe for next lookup
            redisTemplate.opsForValue().set(shortCode,mapping.get().getOriginalUrl(),24, java.util.concurrent.TimeUnit.HOURS);
            return mapping.get().getOriginalUrl();
        }else {
            throw new RuntimeException("Short Url not found");
        }

    }
}

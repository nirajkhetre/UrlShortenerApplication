package in.avaloneco.UrlShortenerApplication.controller;

import in.avaloneco.UrlShortenerApplication.service.UrlShortenerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/url")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(path = "/api/shorten")
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String,String> body){
        String longUrl = body.get("url");
        String shortUrl = urlShortenerService.shortenUrl(longUrl);
        return ResponseEntity.ok(Map.of("shortUrl",shortUrl));
    }
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl = urlShortenerService.getOriginalUrl(shortCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}

package in.avaloneco.UrlShortenerApplication.service;

public interface  UrlShortenerService {
    String shortenUrl(String originalUrl);
    String getOriginalUrl(String shortCode);
}

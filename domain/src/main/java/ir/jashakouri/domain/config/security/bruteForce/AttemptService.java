package ir.jashakouri.domain.config.security.bruteForce;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ir.jashakouri.data.utils.CommonVariables;
import ir.jashakouri.data.utils.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author jashakouri on 28.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Service
@Slf4j(topic = "LOG_LoginAttemptService")
public class AttemptService {

    private final int MAX_ATTEMPT = 3;

    private final LoadingCache<String, Integer> attemptsCache;

    public AttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(MAX_ATTEMPT, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    public @NotNull Integer load(@Nullable String key) {
                        return 0;
                    }
                });
    }

    public void successDetected(HttpServletRequest request) {
        String ip = NetworkUtils.getIp(request);

        log.info("IP: /{}/ | Path: {} | Method: {} | T",
                ip, request.getServletPath(), request.getMethod());

        attemptsCache.invalidate(ip);
    }

    public void failedDetected(HttpServletRequest request) {

        if (Arrays.toString(CommonVariables.SOURCE_WHITELIST).contains(request.getServletPath()))
            return;

        String ip = NetworkUtils.getIp(request);

        int attempts;
        try {
            attempts = attemptsCache.get(ip);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(ip, attempts);

        log.info("IP: /{}/ | Attempt: {} | Path: {} | Method: {} | F",
                ip, attempts, request.getServletPath(), request.getMethod());
    }

    public boolean isBlocked(HttpServletRequest request) {

        if (request.getServletPath().contains(Arrays.toString(CommonVariables.SOURCE_WHITELIST)))
            return false;

        String ip = NetworkUtils.getIp(request);

        try {
            return attemptsCache.get(ip) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

}

package edu.udemy.micronaut.client;

import edu.udemy.micronaut.controller.dto.WatchList;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;

@Client("/")
public interface JWTWatchListClient {

    @Post("/login")
    BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);

    @Get("/accounts/watchlist")
    HttpResponse<WatchList> getWatchlist(@Header String authorization);

    @Get("/accounts/watchlist")
    HttpResponse<WatchList> getWatchlistAsASingleList(@Header String authorization);

    @Put("/accounts/watchlist")
    HttpResponse<WatchList> updateWatchlist(@Header String authorization, @Body WatchList watchList);

    @Delete("/accounts/watchlist")
    HttpResponse<WatchList> deleteWatchlist(@Header String authorization);



}

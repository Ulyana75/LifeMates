package ru.ulyanaab.lifemates.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.ulyanaab.lifemates.data.api.FeedApi
import ru.ulyanaab.lifemates.data.api.TokenApi
import ru.ulyanaab.lifemates.data.api.UserApi
import javax.inject.Named

@Module
object AppModule {

    @AppScope
    @Provides
    fun provideRetrofit(@Named(BASE_URL) baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @AppScope
    @Provides
    fun provideFeedApi(retrofit: Retrofit): FeedApi {
        return retrofit.create(FeedApi::class.java)
    }

    @AppScope
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @AppScope
    @Provides
    fun provideTokenApi(retrofit: Retrofit): TokenApi {
        return retrofit.create(TokenApi::class.java)
    }
}

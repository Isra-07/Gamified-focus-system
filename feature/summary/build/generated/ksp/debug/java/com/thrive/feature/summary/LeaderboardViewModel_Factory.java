package com.thrive.feature.summary;

import com.thrive.domain.UserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class LeaderboardViewModel_Factory implements Factory<LeaderboardViewModel> {
  private final Provider<UserRepository> userRepositoryProvider;

  public LeaderboardViewModel_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public LeaderboardViewModel get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static LeaderboardViewModel_Factory create(
      Provider<UserRepository> userRepositoryProvider) {
    return new LeaderboardViewModel_Factory(userRepositoryProvider);
  }

  public static LeaderboardViewModel newInstance(UserRepository userRepository) {
    return new LeaderboardViewModel(userRepository);
  }
}

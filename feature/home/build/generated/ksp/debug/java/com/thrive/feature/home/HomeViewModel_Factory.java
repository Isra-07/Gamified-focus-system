package com.thrive.feature.home;

import com.thrive.domain.SessionRepository;
import com.thrive.domain.UserRepository;
import com.thrive.domain.repository.ChallengeRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<ChallengeRepository> challengeRepositoryProvider;

  public HomeViewModel_Factory(Provider<UserRepository> userRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<ChallengeRepository> challengeRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.challengeRepositoryProvider = challengeRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(userRepositoryProvider.get(), sessionRepositoryProvider.get(), challengeRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<UserRepository> userRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<ChallengeRepository> challengeRepositoryProvider) {
    return new HomeViewModel_Factory(userRepositoryProvider, sessionRepositoryProvider, challengeRepositoryProvider);
  }

  public static HomeViewModel newInstance(UserRepository userRepository,
      SessionRepository sessionRepository, ChallengeRepository challengeRepository) {
    return new HomeViewModel(userRepository, sessionRepository, challengeRepository);
  }
}

package com.thrive.feature.challenge;

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
public final class ChallengeDetailViewModel_Factory implements Factory<ChallengeDetailViewModel> {
  private final Provider<ChallengeRepository> challengeRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public ChallengeDetailViewModel_Factory(Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.challengeRepositoryProvider = challengeRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public ChallengeDetailViewModel get() {
    return newInstance(challengeRepositoryProvider.get(), userRepositoryProvider.get());
  }

  public static ChallengeDetailViewModel_Factory create(
      Provider<ChallengeRepository> challengeRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new ChallengeDetailViewModel_Factory(challengeRepositoryProvider, userRepositoryProvider);
  }

  public static ChallengeDetailViewModel newInstance(ChallengeRepository challengeRepository,
      UserRepository userRepository) {
    return new ChallengeDetailViewModel(challengeRepository, userRepository);
  }
}

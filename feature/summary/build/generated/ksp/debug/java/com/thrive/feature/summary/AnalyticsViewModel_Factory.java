package com.thrive.feature.summary;

import com.thrive.domain.SessionRepository;
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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  public AnalyticsViewModel_Factory(Provider<SessionRepository> sessionRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(sessionRepositoryProvider.get(), userRepositoryProvider.get());
  }

  public static AnalyticsViewModel_Factory create(
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new AnalyticsViewModel_Factory(sessionRepositoryProvider, userRepositoryProvider);
  }

  public static AnalyticsViewModel newInstance(SessionRepository sessionRepository,
      UserRepository userRepository) {
    return new AnalyticsViewModel(sessionRepository, userRepository);
  }
}

package com.thrive.feature.timer;

import android.content.Context;
import com.thrive.data.distraction.DistractionDetector;
import com.thrive.domain.DistractionRepository;
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
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class TimerViewModel_Factory implements Factory<TimerViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<DistractionRepository> distractionRepositoryProvider;

  private final Provider<DistractionDetector> detectorProvider;

  private final Provider<ChallengeRepository> challengeRepositoryProvider;

  public TimerViewModel_Factory(Provider<Context> contextProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<DistractionRepository> distractionRepositoryProvider,
      Provider<DistractionDetector> detectorProvider,
      Provider<ChallengeRepository> challengeRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.distractionRepositoryProvider = distractionRepositoryProvider;
    this.detectorProvider = detectorProvider;
    this.challengeRepositoryProvider = challengeRepositoryProvider;
  }

  @Override
  public TimerViewModel get() {
    return newInstance(contextProvider.get(), sessionRepositoryProvider.get(), userRepositoryProvider.get(), distractionRepositoryProvider.get(), detectorProvider.get(), challengeRepositoryProvider.get());
  }

  public static TimerViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<DistractionRepository> distractionRepositoryProvider,
      Provider<DistractionDetector> detectorProvider,
      Provider<ChallengeRepository> challengeRepositoryProvider) {
    return new TimerViewModel_Factory(contextProvider, sessionRepositoryProvider, userRepositoryProvider, distractionRepositoryProvider, detectorProvider, challengeRepositoryProvider);
  }

  public static TimerViewModel newInstance(Context context, SessionRepository sessionRepository,
      UserRepository userRepository, DistractionRepository distractionRepository,
      DistractionDetector detector, ChallengeRepository challengeRepository) {
    return new TimerViewModel(context, sessionRepository, userRepository, distractionRepository, detector, challengeRepository);
  }
}

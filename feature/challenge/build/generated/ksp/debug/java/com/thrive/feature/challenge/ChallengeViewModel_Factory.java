package com.thrive.feature.challenge;

import com.thrive.domain.usecase.EvaluateChallengesUseCase;
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
    "cast",
    "deprecation"
})
public final class ChallengeViewModel_Factory implements Factory<ChallengeViewModel> {
  private final Provider<EvaluateChallengesUseCase> evaluateChallengesProvider;

  public ChallengeViewModel_Factory(
      Provider<EvaluateChallengesUseCase> evaluateChallengesProvider) {
    this.evaluateChallengesProvider = evaluateChallengesProvider;
  }

  @Override
  public ChallengeViewModel get() {
    return newInstance(evaluateChallengesProvider.get());
  }

  public static ChallengeViewModel_Factory create(
      Provider<EvaluateChallengesUseCase> evaluateChallengesProvider) {
    return new ChallengeViewModel_Factory(evaluateChallengesProvider);
  }

  public static ChallengeViewModel newInstance(EvaluateChallengesUseCase evaluateChallenges) {
    return new ChallengeViewModel(evaluateChallenges);
  }
}

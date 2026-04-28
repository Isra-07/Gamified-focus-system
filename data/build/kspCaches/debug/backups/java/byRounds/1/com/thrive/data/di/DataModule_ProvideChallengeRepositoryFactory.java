package com.thrive.data.di;

import com.thrive.data.repository.ChallengeRepositoryImpl;
import com.thrive.domain.repository.ChallengeRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DataModule_ProvideChallengeRepositoryFactory implements Factory<ChallengeRepository> {
  private final Provider<ChallengeRepositoryImpl> implProvider;

  public DataModule_ProvideChallengeRepositoryFactory(
      Provider<ChallengeRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public ChallengeRepository get() {
    return provideChallengeRepository(implProvider.get());
  }

  public static DataModule_ProvideChallengeRepositoryFactory create(
      Provider<ChallengeRepositoryImpl> implProvider) {
    return new DataModule_ProvideChallengeRepositoryFactory(implProvider);
  }

  public static ChallengeRepository provideChallengeRepository(ChallengeRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideChallengeRepository(impl));
  }
}

package com.thrive.data.di;

import com.thrive.data.repository.AnalyticsRepositoryImpl;
import com.thrive.domain.repository.AnalyticsRepository;
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
public final class DataModule_ProvideAnalyticsRepositoryFactory implements Factory<AnalyticsRepository> {
  private final Provider<AnalyticsRepositoryImpl> implProvider;

  public DataModule_ProvideAnalyticsRepositoryFactory(
      Provider<AnalyticsRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public AnalyticsRepository get() {
    return provideAnalyticsRepository(implProvider.get());
  }

  public static DataModule_ProvideAnalyticsRepositoryFactory create(
      Provider<AnalyticsRepositoryImpl> implProvider) {
    return new DataModule_ProvideAnalyticsRepositoryFactory(implProvider);
  }

  public static AnalyticsRepository provideAnalyticsRepository(AnalyticsRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideAnalyticsRepository(impl));
  }
}

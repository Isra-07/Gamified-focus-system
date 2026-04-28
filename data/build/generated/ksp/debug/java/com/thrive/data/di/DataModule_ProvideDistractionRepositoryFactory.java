package com.thrive.data.di;

import com.thrive.data.repository.DistractionRepositoryImpl;
import com.thrive.domain.DistractionRepository;
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
public final class DataModule_ProvideDistractionRepositoryFactory implements Factory<DistractionRepository> {
  private final Provider<DistractionRepositoryImpl> implProvider;

  public DataModule_ProvideDistractionRepositoryFactory(
      Provider<DistractionRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public DistractionRepository get() {
    return provideDistractionRepository(implProvider.get());
  }

  public static DataModule_ProvideDistractionRepositoryFactory create(
      Provider<DistractionRepositoryImpl> implProvider) {
    return new DataModule_ProvideDistractionRepositoryFactory(implProvider);
  }

  public static DistractionRepository provideDistractionRepository(DistractionRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideDistractionRepository(impl));
  }
}

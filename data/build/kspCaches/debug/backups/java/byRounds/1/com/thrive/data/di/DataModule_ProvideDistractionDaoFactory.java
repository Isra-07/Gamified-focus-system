package com.thrive.data.di;

import com.thrive.data.local.DistractionDao;
import com.thrive.data.local.ThriveDatabase;
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
public final class DataModule_ProvideDistractionDaoFactory implements Factory<DistractionDao> {
  private final Provider<ThriveDatabase> dbProvider;

  public DataModule_ProvideDistractionDaoFactory(Provider<ThriveDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DistractionDao get() {
    return provideDistractionDao(dbProvider.get());
  }

  public static DataModule_ProvideDistractionDaoFactory create(
      Provider<ThriveDatabase> dbProvider) {
    return new DataModule_ProvideDistractionDaoFactory(dbProvider);
  }

  public static DistractionDao provideDistractionDao(ThriveDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideDistractionDao(db));
  }
}

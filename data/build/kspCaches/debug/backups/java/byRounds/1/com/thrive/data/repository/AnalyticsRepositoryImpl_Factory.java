package com.thrive.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AnalyticsRepositoryImpl_Factory implements Factory<AnalyticsRepositoryImpl> {
  @Override
  public AnalyticsRepositoryImpl get() {
    return newInstance();
  }

  public static AnalyticsRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AnalyticsRepositoryImpl newInstance() {
    return new AnalyticsRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final AnalyticsRepositoryImpl_Factory INSTANCE = new AnalyticsRepositoryImpl_Factory();
  }
}

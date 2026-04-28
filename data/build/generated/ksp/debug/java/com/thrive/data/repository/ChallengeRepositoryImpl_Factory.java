package com.thrive.data.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ChallengeRepositoryImpl_Factory implements Factory<ChallengeRepositoryImpl> {
  private final Provider<Context> contextProvider;

  public ChallengeRepositoryImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ChallengeRepositoryImpl get() {
    return newInstance(contextProvider.get());
  }

  public static ChallengeRepositoryImpl_Factory create(Provider<Context> contextProvider) {
    return new ChallengeRepositoryImpl_Factory(contextProvider);
  }

  public static ChallengeRepositoryImpl newInstance(Context context) {
    return new ChallengeRepositoryImpl(context);
  }
}

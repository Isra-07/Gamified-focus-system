package com.thrive.data.distraction;

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
public final class DistractionDetector_Factory implements Factory<DistractionDetector> {
  private final Provider<Context> contextProvider;

  public DistractionDetector_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DistractionDetector get() {
    return newInstance(contextProvider.get());
  }

  public static DistractionDetector_Factory create(Provider<Context> contextProvider) {
    return new DistractionDetector_Factory(contextProvider);
  }

  public static DistractionDetector newInstance(Context context) {
    return new DistractionDetector(context);
  }
}

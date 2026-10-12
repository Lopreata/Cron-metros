import { useState, useEffect, useCallback, useRef } from 'react';
import { AppState, AppStateStatus } from 'react-native';
import { Counter, CounterStats } from '../types/Counter';
import { loadCounters, saveCounters } from '../storage/counterStorage';

function calcStats(counter: Counter, now: number): CounterStats {
  let effectiveMs: number;
  if (counter.pausedAt !== null) {
    effectiveMs = counter.pausedAt - counter.startedAt - counter.totalPausedMs;
  } else {
    effectiveMs = now - counter.startedAt - counter.totalPausedMs;
  }
  effectiveMs = Math.max(0, effectiveMs);

  const days = Math.floor(effectiveMs / (1000 * 60 * 60 * 24));
  const remainingAfterDays = effectiveMs % (1000 * 60 * 60 * 24);
  const hours = Math.floor(remainingAfterDays / (1000 * 60 * 60));
  const remainingAfterHours = remainingAfterDays % (1000 * 60 * 60);
  const minutes = Math.floor(remainingAfterHours / (1000 * 60));

  return {
    days,
    hours,
    minutes,
    totalMs: effectiveMs,
    status: counter.pausedAt !== null ? 'paused' : 'running',
  };
}

export function useCounters() {
  const [counters, setCounters] = useState<Counter[]>([]);
  const [tick, setTick] = useState(0);
  const [loaded, setLoaded] = useState(false);
  const appState = useRef(AppState.currentState);

  useEffect(() => {
    loadCounters().then((data) => {
      setCounters(data);
      setLoaded(true);
    });
  }, []);

  // Save whenever counters change (after initial load)
  useEffect(() => {
    if (loaded) {
      saveCounters(counters);
    }
  }, [counters, loaded]);

  // Tick every minute to refresh UI
  useEffect(() => {
    const id = setInterval(() => setTick((t) => t + 1), 60_000);
    return () => clearInterval(id);
  }, []);

  // Refresh on app foreground
  useEffect(() => {
    const sub = AppState.addEventListener('change', (next: AppStateStatus) => {
      if (appState.current.match(/inactive|background/) && next === 'active') {
        setTick((t) => t + 1);
      }
      appState.current = next;
    });
    return () => sub.remove();
  }, []);

  const getStats = useCallback(
    (counter: Counter): CounterStats => calcStats(counter, Date.now()),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [tick],
  );

  const addCounter = useCallback((counter: Counter) => {
    setCounters((prev) => [...prev, counter]);
  }, []);

  const removeCounter = useCallback((id: string) => {
    setCounters((prev) => prev.filter((c) => c.id !== id));
  }, []);

  const pauseCounter = useCallback((id: string) => {
    setCounters((prev) =>
      prev.map((c) =>
        c.id === id && c.pausedAt === null
          ? { ...c, pausedAt: Date.now(), isActive: false }
          : c,
      ),
    );
  }, []);

  const resumeCounter = useCallback((id: string) => {
    setCounters((prev) =>
      prev.map((c) => {
        if (c.id !== id || c.pausedAt === null) return c;
        const pausedDuration = Date.now() - c.pausedAt;
        return {
          ...c,
          pausedAt: null,
          totalPausedMs: c.totalPausedMs + pausedDuration,
          isActive: true,
        };
      }),
    );
  }, []);

  const resetCounter = useCallback((id: string) => {
    setCounters((prev) =>
      prev.map((c) =>
        c.id === id
          ? { ...c, startedAt: Date.now(), pausedAt: null, totalPausedMs: 0, isActive: true }
          : c,
      ),
    );
  }, []);

  const pauseMultiple = useCallback((ids: string[]) => {
    const now = Date.now();
    setCounters((prev) =>
      prev.map((c) =>
        ids.includes(c.id) && c.pausedAt === null
          ? { ...c, pausedAt: now, isActive: false }
          : c,
      ),
    );
  }, []);

  const resumeMultiple = useCallback((ids: string[]) => {
    const now = Date.now();
    setCounters((prev) =>
      prev.map((c) => {
        if (!ids.includes(c.id) || c.pausedAt === null) return c;
        return {
          ...c,
          pausedAt: null,
          totalPausedMs: c.totalPausedMs + (now - c.pausedAt),
          isActive: true,
        };
      }),
    );
  }, []);

  const resetMultiple = useCallback((ids: string[]) => {
    const now = Date.now();
    setCounters((prev) =>
      prev.map((c) =>
        ids.includes(c.id)
          ? { ...c, startedAt: now, pausedAt: null, totalPausedMs: 0, isActive: true }
          : c,
      ),
    );
  }, []);

  const removeMultiple = useCallback((ids: string[]) => {
    setCounters((prev) => prev.filter((c) => !ids.includes(c.id)));
  }, []);

  return {
    counters,
    loaded,
    getStats,
    addCounter,
    removeCounter,
    pauseCounter,
    resumeCounter,
    resetCounter,
    pauseMultiple,
    resumeMultiple,
    resetMultiple,
    removeMultiple,
  };
}

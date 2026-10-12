import AsyncStorage from '@react-native-async-storage/async-storage';
import { Counter } from '../types/Counter';

const STORAGE_KEY = '@cron_metros_counters';

export async function loadCounters(): Promise<Counter[]> {
  try {
    const json = await AsyncStorage.getItem(STORAGE_KEY);
    if (!json) return [];
    return JSON.parse(json) as Counter[];
  } catch {
    return [];
  }
}

export async function saveCounters(counters: Counter[]): Promise<void> {
  try {
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(counters));
  } catch {
    // ignore
  }
}

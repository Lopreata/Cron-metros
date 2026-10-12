export interface Counter {
  id: string;
  name: string;
  emoji: string;
  description: string;
  startedAt: number;       // timestamp (ms) when current streak started / last reset
  pausedAt: number | null; // timestamp when paused, null if running
  totalPausedMs: number;   // total accumulated paused milliseconds
  isActive: boolean;
  createdAt: number;
  color: string;
}

export type CounterStatus = 'running' | 'paused';

export interface CounterStats {
  days: number;
  hours: number;
  minutes: number;
  totalMs: number;
  status: CounterStatus;
}

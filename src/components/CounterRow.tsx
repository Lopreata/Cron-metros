import React from 'react';
import { StyleSheet, View, TouchableOpacity } from 'react-native';
import { Text, Checkbox, IconButton, Chip } from 'react-native-paper';
import { Counter, CounterStats } from '../types/Counter';

interface Props {
  counter: Counter;
  stats: CounterStats;
  selected: boolean;
  onSelect: (id: string) => void;
  onPause: (id: string) => void;
  onResume: (id: string) => void;
  onReset: (id: string) => void;
  onDelete: (id: string) => void;
}

export function CounterRow({
  counter,
  stats,
  selected,
  onSelect,
  onPause,
  onResume,
  onReset,
  onDelete,
}: Props) {
  const isPaused = stats.status === 'paused';

  return (
    <TouchableOpacity
      onPress={() => onSelect(counter.id)}
      activeOpacity={0.8}
      style={[styles.row, selected && styles.rowSelected]}
    >
      <View style={styles.checkboxCol}>
        <Checkbox
          status={selected ? 'checked' : 'unchecked'}
          onPress={() => onSelect(counter.id)}
          color="#CBA6F7"
          uncheckedColor="#585B70"
        />
      </View>

      <View style={[styles.colorBar, { backgroundColor: counter.color }]} />

      <View style={styles.emojiCol}>
        <Text style={styles.emoji}>{counter.emoji}</Text>
      </View>

      <View style={styles.infoCol}>
        <View style={styles.nameRow}>
          <Text variant="titleSmall" style={styles.name} numberOfLines={1}>
            {counter.name}
          </Text>
          {isPaused && (
            <Chip
              compact
              style={styles.pausedChip}
              textStyle={styles.pausedChipText}
              icon="pause"
            >
              Pausado
            </Chip>
          )}
        </View>
        <Text variant="bodySmall" style={styles.description} numberOfLines={1}>
          {counter.description || ' '}
        </Text>
      </View>

      <View style={styles.daysCol}>
        <Text style={[styles.daysNumber, { color: counter.color }]}>
          {stats.days}
        </Text>
        <Text style={styles.daysLabel}>dias</Text>
        <Text style={styles.timeLabel}>
          {String(stats.hours).padStart(2, '0')}h {String(stats.minutes).padStart(2, '0')}m
        </Text>
      </View>

      <View style={styles.actionsCol}>
        {isPaused ? (
          <IconButton
            icon="play"
            iconColor="#A6E3A1"
            size={20}
            onPress={() => onResume(counter.id)}
            style={styles.iconBtn}
          />
        ) : (
          <IconButton
            icon="pause"
            iconColor="#FAB387"
            size={20}
            onPress={() => onPause(counter.id)}
            style={styles.iconBtn}
          />
        )}
        <IconButton
          icon="restart"
          iconColor="#89B4FA"
          size={20}
          onPress={() => onReset(counter.id)}
          style={styles.iconBtn}
        />
        <IconButton
          icon="delete-outline"
          iconColor="#F38BA8"
          size={20}
          onPress={() => onDelete(counter.id)}
          style={styles.iconBtn}
        />
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#1E1E2E',
    marginHorizontal: 12,
    marginVertical: 4,
    borderRadius: 12,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: '#313244',
  },
  rowSelected: {
    borderColor: '#CBA6F7',
    backgroundColor: '#2A273F',
  },
  checkboxCol: { paddingLeft: 4 },
  colorBar: { width: 4, alignSelf: 'stretch' },
  emojiCol: {
    width: 44,
    alignItems: 'center',
    justifyContent: 'center',
  },
  emoji: { fontSize: 24 },
  infoCol: { flex: 1, paddingVertical: 10, paddingLeft: 4, paddingRight: 4 },
  nameRow: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  name: { color: '#CDD6F4', flex: 1 },
  description: { color: '#6C7086', marginTop: 2 },
  pausedChip: { backgroundColor: '#45475A', height: 22 },
  pausedChipText: { color: '#FAB387', fontSize: 10, lineHeight: 14 },
  daysCol: { alignItems: 'center', minWidth: 54, paddingHorizontal: 4 },
  daysNumber: { fontSize: 26, fontWeight: '700', lineHeight: 30 },
  daysLabel: { color: '#6C7086', fontSize: 11, lineHeight: 14 },
  timeLabel: { color: '#585B70', fontSize: 10 },
  actionsCol: { flexDirection: 'row', alignItems: 'center' },
  iconBtn: { margin: 0 },
});

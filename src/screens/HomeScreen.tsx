import React, { useState, useCallback } from 'react';
import {
  StyleSheet,
  View,
  FlatList,
  Alert,
  StatusBar,
} from 'react-native';
import {
  FAB,
  Text,
  Appbar,
  ActivityIndicator,
  Checkbox,
} from 'react-native-paper';
import { SafeAreaView } from 'react-native-safe-area-context';

import { useCounters } from '../hooks/useCounters';
import { Counter } from '../types/Counter';
import { CounterRow } from '../components/CounterRow';
import { AddCounterModal } from '../components/AddCounterModal';
import { BulkActionBar } from '../components/BulkActionBar';
import { ConfirmDialog } from '../components/ConfirmDialog';

export function HomeScreen() {
  const {
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
  } = useCounters();

  const [addVisible, setAddVisible] = useState(false);
  const [selected, setSelected] = useState<Set<string>>(new Set());
  const [confirmState, setConfirmState] = useState<{
    visible: boolean;
    title: string;
    message: string;
    label: string;
    action: () => void;
    destructive: boolean;
  }>({ visible: false, title: '', message: '', label: '', action: () => {}, destructive: false });

  const toggleSelect = useCallback((id: string) => {
    setSelected((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }, []);

  const selectAll = useCallback(() => {
    setSelected(new Set(counters.map((c) => c.id)));
  }, [counters]);

  const clearSelection = useCallback(() => setSelected(new Set()), []);

  function confirmAction(
    title: string,
    message: string,
    label: string,
    action: () => void,
    destructive = false,
  ) {
    setConfirmState({ visible: true, title, message, label, action, destructive });
  }

  function handleDelete(id: string) {
    confirmAction(
      'Excluir cronômetro',
      'Tem certeza que deseja excluir este cronômetro? Esta ação não pode ser desfeita.',
      'Excluir',
      () => removeCounter(id),
      true,
    );
  }

  function handleReset(id: string) {
    confirmAction(
      'Zerar cronômetro',
      'Deseja zerar este cronômetro? A contagem vai recomeçar do zero.',
      'Zerar',
      () => resetCounter(id),
    );
  }

  function handleBulkPause() {
    const ids = Array.from(selected);
    pauseMultiple(ids);
    clearSelection();
  }

  function handleBulkResume() {
    const ids = Array.from(selected);
    resumeMultiple(ids);
    clearSelection();
  }

  function handleBulkReset() {
    confirmAction(
      'Zerar selecionados',
      `Deseja zerar ${selected.size} cronômetro${selected.size > 1 ? 's' : ''}?`,
      'Zerar',
      () => {
        resetMultiple(Array.from(selected));
        clearSelection();
      },
    );
  }

  function handleBulkDelete() {
    confirmAction(
      'Excluir selecionados',
      `Deseja excluir ${selected.size} cronômetro${selected.size > 1 ? 's' : ''}? Esta ação não pode ser desfeita.`,
      'Excluir',
      () => {
        removeMultiple(Array.from(selected));
        clearSelection();
      },
      true,
    );
  }

  const allSelected = counters.length > 0 && selected.size === counters.length;

  const renderItem = useCallback(
    ({ item }: { item: Counter }) => (
      <CounterRow
        counter={item}
        stats={getStats(item)}
        selected={selected.has(item.id)}
        onSelect={toggleSelect}
        onPause={pauseCounter}
        onResume={resumeCounter}
        onReset={handleReset}
        onDelete={handleDelete}
      />
    ),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [selected, getStats],
  );

  const headerRow = counters.length > 0 ? (
    <View style={styles.tableHeader}>
      <Checkbox
        status={allSelected ? 'checked' : selected.size > 0 ? 'indeterminate' : 'unchecked'}
        onPress={allSelected ? clearSelection : selectAll}
        color="#CBA6F7"
        uncheckedColor="#585B70"
      />
      <Text style={[styles.headerCell, { flex: 1, marginLeft: 28 }]}>Cronômetro</Text>
      <Text style={[styles.headerCell, styles.daysHeader]}>Dias</Text>
      <Text style={[styles.headerCell, styles.actionsHeader]}>Ações</Text>
    </View>
  ) : null;

  return (
    <SafeAreaView style={styles.container} edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor="#181825" />

      <Appbar.Header style={styles.appbar}>
        <Appbar.Content
          title="Cron-metros"
          titleStyle={styles.appbarTitle}
          subtitle={`${counters.length} cronômetro${counters.length !== 1 ? 's' : ''}`}
          subtitleStyle={styles.appbarSubtitle}
        />
      </Appbar.Header>

      {!loaded ? (
        <View style={styles.center}>
          <ActivityIndicator color="#CBA6F7" size="large" />
        </View>
      ) : counters.length === 0 ? (
        <View style={styles.center}>
          <Text style={styles.emptyEmoji}>⏱️</Text>
          <Text variant="titleMedium" style={styles.emptyTitle}>
            Nenhum cronômetro ainda
          </Text>
          <Text variant="bodyMedium" style={styles.emptyText}>
            Toque no botão + para criar{'\n'}seu primeiro contador de dias
          </Text>
        </View>
      ) : (
        <FlatList
          data={counters}
          keyExtractor={(item) => item.id}
          renderItem={renderItem}
          ListHeaderComponent={headerRow}
          contentContainerStyle={styles.list}
          showsVerticalScrollIndicator={false}
        />
      )}

      <BulkActionBar
        selectedCount={selected.size}
        onPause={handleBulkPause}
        onResume={handleBulkResume}
        onReset={handleBulkReset}
        onDelete={handleBulkDelete}
        onClearSelection={clearSelection}
      />

      <FAB
        icon="plus"
        style={styles.fab}
        onPress={() => setAddVisible(true)}
        color="#1E1E2E"
      />

      <AddCounterModal
        visible={addVisible}
        onDismiss={() => setAddVisible(false)}
        onAdd={addCounter}
      />

      <ConfirmDialog
        visible={confirmState.visible}
        title={confirmState.title}
        message={confirmState.message}
        confirmLabel={confirmState.label}
        onConfirm={confirmState.action}
        onDismiss={() => setConfirmState((s) => ({ ...s, visible: false }))}
        destructive={confirmState.destructive}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#181825' },
  appbar: { backgroundColor: '#181825', elevation: 0 },
  appbarTitle: { color: '#CDD6F4', fontWeight: '700', fontSize: 20 },
  appbarSubtitle: { color: '#6C7086', fontSize: 12 },
  center: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 32 },
  emptyEmoji: { fontSize: 56, marginBottom: 16 },
  emptyTitle: { color: '#CDD6F4', marginBottom: 8 },
  emptyText: { color: '#6C7086', textAlign: 'center', lineHeight: 22 },
  tableHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 6,
    marginHorizontal: 12,
    marginTop: 4,
  },
  headerCell: { color: '#585B70', fontSize: 12, fontWeight: '600', letterSpacing: 0.5 },
  daysHeader: { minWidth: 54, textAlign: 'center' },
  actionsHeader: { width: 120, textAlign: 'center' },
  list: { paddingBottom: 80 },
  fab: {
    position: 'absolute',
    right: 20,
    bottom: 20,
    backgroundColor: '#CBA6F7',
  },
});

import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Button, Text } from 'react-native-paper';

interface Props {
  selectedCount: number;
  onPause: () => void;
  onResume: () => void;
  onReset: () => void;
  onDelete: () => void;
  onClearSelection: () => void;
}

export function BulkActionBar({
  selectedCount,
  onPause,
  onResume,
  onReset,
  onDelete,
  onClearSelection,
}: Props) {
  if (selectedCount === 0) return null;

  return (
    <View style={styles.bar}>
      <Text style={styles.count}>{selectedCount} selecionado{selectedCount > 1 ? 's' : ''}</Text>
      <Button
        compact
        mode="text"
        icon="play"
        textColor="#A6E3A1"
        onPress={onResume}
        style={styles.btn}
      >
        Iniciar
      </Button>
      <Button
        compact
        mode="text"
        icon="pause"
        textColor="#FAB387"
        onPress={onPause}
        style={styles.btn}
      >
        Pausar
      </Button>
      <Button
        compact
        mode="text"
        icon="restart"
        textColor="#89B4FA"
        onPress={onReset}
        style={styles.btn}
      >
        Zerar
      </Button>
      <Button
        compact
        mode="text"
        icon="delete-outline"
        textColor="#F38BA8"
        onPress={onDelete}
        style={styles.btn}
      >
        Excluir
      </Button>
      <Button
        compact
        mode="text"
        icon="close"
        textColor="#6C7086"
        onPress={onClearSelection}
        style={styles.btn}
      >
        {''}
      </Button>
    </View>
  );
}

const styles = StyleSheet.create({
  bar: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#2A273F',
    paddingHorizontal: 8,
    paddingVertical: 6,
    borderTopWidth: 1,
    borderTopColor: '#CBA6F7',
  },
  count: { color: '#CBA6F7', fontWeight: '600', marginRight: 4, fontSize: 13 },
  btn: { minWidth: 0, paddingHorizontal: 0 },
});

import React, { useState } from 'react';
import { StyleSheet, View, ScrollView, TouchableOpacity } from 'react-native';
import {
  Modal,
  Portal,
  Text,
  TextInput,
  Button,
  Surface,
} from 'react-native-paper';
import { Counter } from '../types/Counter';

const EMOJIS = [
  '😴', '💪', '🚭', '🍺', '🍕', '💊', '🏃', '📚',
  '🧘', '💰', '❤️', '🌱', '🎯', '✨', '🔥', '⭐',
  '🏆', '🎵', '🧹', '💻', '🌙', '☀️', '🍎', '💧',
];

const COLORS = [
  '#6366F1', '#8B5CF6', '#EC4899', '#EF4444',
  '#F97316', '#EAB308', '#22C55E', '#14B8A6',
  '#3B82F6', '#06B6D4', '#84CC16', '#F43F5E',
];

const SUGGESTIONS = [
  'Dias dormindo bem', 'Dias sem recaída', 'Dias sem fumar',
  'Dias sem álcool', 'Dias se exercitando', 'Dias meditando',
  'Dias estudando', 'Dias sem açúcar', 'Dias bebendo água',
  'Dias de dieta', 'Dias lendo', 'Dias sem redes sociais',
];

interface Props {
  visible: boolean;
  onDismiss: () => void;
  onAdd: (counter: Counter) => void;
}

export function AddCounterModal({ visible, onDismiss, onAdd }: Props) {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [emoji, setEmoji] = useState('⭐');
  const [color, setColor] = useState(COLORS[0]);

  function handleAdd() {
    if (!name.trim()) return;
    const now = Date.now();
    const counter: Counter = {
      id: `${now}_${Math.random().toString(36).slice(2)}`,
      name: name.trim(),
      emoji,
      description: description.trim(),
      startedAt: now,
      pausedAt: null,
      totalPausedMs: 0,
      isActive: true,
      createdAt: now,
      color,
    };
    onAdd(counter);
    setName('');
    setDescription('');
    setEmoji('⭐');
    setColor(COLORS[0]);
    onDismiss();
  }

  return (
    <Portal>
      <Modal visible={visible} onDismiss={onDismiss} contentContainerStyle={styles.modal}>
        <ScrollView showsVerticalScrollIndicator={false}>
          <Text variant="titleLarge" style={styles.title}>Novo Cronômetro</Text>

          <TextInput
            label="Nome do cronômetro *"
            value={name}
            onChangeText={setName}
            mode="outlined"
            style={styles.input}
            placeholder="Ex: Dias dormindo bem"
          />

          <TextInput
            label="Descrição (opcional)"
            value={description}
            onChangeText={setDescription}
            mode="outlined"
            style={styles.input}
            placeholder="Detalhes sobre este contador"
          />

          <Text variant="labelLarge" style={styles.sectionLabel}>Sugestões</Text>
          <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.suggestions}>
            {SUGGESTIONS.map((s) => (
              <TouchableOpacity key={s} onPress={() => setName(s)} style={styles.chip}>
                <Text style={styles.chipText}>{s}</Text>
              </TouchableOpacity>
            ))}
          </ScrollView>

          <Text variant="labelLarge" style={styles.sectionLabel}>Emoji</Text>
          <View style={styles.grid}>
            {EMOJIS.map((e) => (
              <TouchableOpacity
                key={e}
                onPress={() => setEmoji(e)}
                style={[styles.emojiBtn, emoji === e && styles.emojiSelected]}
              >
                <Text style={styles.emojiText}>{e}</Text>
              </TouchableOpacity>
            ))}
          </View>

          <Text variant="labelLarge" style={styles.sectionLabel}>Cor</Text>
          <View style={styles.grid}>
            {COLORS.map((c) => (
              <TouchableOpacity
                key={c}
                onPress={() => setColor(c)}
                style={[styles.colorBtn, { backgroundColor: c }, color === c && styles.colorSelected]}
              />
            ))}
          </View>

          <View style={styles.preview}>
            <Surface style={[styles.previewCard, { borderLeftColor: color }]} elevation={1}>
              <Text style={styles.previewEmoji}>{emoji}</Text>
              <Text variant="titleMedium" style={styles.previewName}>
                {name || 'Nome do cronômetro'}
              </Text>
            </Surface>
          </View>

          <View style={styles.actions}>
            <Button mode="text" onPress={onDismiss} style={styles.btn}>Cancelar</Button>
            <Button mode="contained" onPress={handleAdd} disabled={!name.trim()} style={styles.btn}>
              Criar
            </Button>
          </View>
        </ScrollView>
      </Modal>
    </Portal>
  );
}

const styles = StyleSheet.create({
  modal: {
    backgroundColor: '#1E1E2E',
    margin: 20,
    borderRadius: 16,
    padding: 20,
    maxHeight: '90%',
  },
  title: { color: '#CDD6F4', marginBottom: 16, textAlign: 'center' },
  input: { marginBottom: 12, backgroundColor: '#313244' },
  sectionLabel: { color: '#A6ADC8', marginTop: 12, marginBottom: 8 },
  suggestions: { marginBottom: 4 },
  chip: {
    backgroundColor: '#313244',
    borderRadius: 20,
    paddingHorizontal: 12,
    paddingVertical: 6,
    marginRight: 8,
    borderWidth: 1,
    borderColor: '#45475A',
  },
  chipText: { color: '#CDD6F4', fontSize: 12 },
  grid: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 8 },
  emojiBtn: {
    width: 44,
    height: 44,
    borderRadius: 8,
    backgroundColor: '#313244',
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 2,
    borderColor: 'transparent',
  },
  emojiSelected: { borderColor: '#CBA6F7' },
  emojiText: { fontSize: 22 },
  colorBtn: {
    width: 36,
    height: 36,
    borderRadius: 18,
    borderWidth: 3,
    borderColor: 'transparent',
  },
  colorSelected: { borderColor: '#CDD6F4' },
  preview: { marginTop: 16, marginBottom: 8 },
  previewCard: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderRadius: 12,
    backgroundColor: '#313244',
    borderLeftWidth: 4,
  },
  previewEmoji: { fontSize: 28, marginRight: 12 },
  previewName: { color: '#CDD6F4', flex: 1 },
  actions: { flexDirection: 'row', justifyContent: 'flex-end', marginTop: 16, gap: 8 },
  btn: { minWidth: 100 },
});

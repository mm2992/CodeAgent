const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8848/api').replace(/\/$/, '');
const DONE_MARKERS = new Set(['[DONE]', 'DONE', '__DONE__']);

function openChatStream(path, params, handlers) {
  const query = new URLSearchParams(params);
  const source = new EventSource(`${API_BASE_URL}${path}?${query.toString()}`);
  let settled = false;
  let receivedAnyChunk = false;

  const finish = () => {
    if (settled) return;
    settled = true;
    source.close();
    handlers.onDone?.();
  };

  source.onmessage = (event) => {
    const chunk = event.data ?? '';

    if (DONE_MARKERS.has(chunk.trim())) {
      finish();
      return;
    }

    receivedAnyChunk = true;
    handlers.onChunk?.(chunk);
  };

  source.addEventListener('done', finish);

  source.onerror = () => {
    if (settled) return;
    settled = true;
    source.close();

    if (receivedAnyChunk) {
      handlers.onDone?.();
      return;
    }

    handlers.onError?.(new Error('SSE connection failed'));
  };

  return () => {
    settled = true;
    source.close();
  };
}

export function streamLoveChat({ message, chatId, onChunk, onDone, onError }) {
  return openChatStream(
    '/ai/chat/sse',
    {
      message,
      chatId,
    },
    {
      onChunk,
      onDone,
      onError,
    },
  );
}

export function streamManusChat({ message, onChunk, onDone, onError }) {
  return openChatStream(
    '/ai/manus/chat',
    {
      message,
    },
    {
      onChunk,
      onDone,
      onError,
    },
  );
}

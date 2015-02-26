package axion.domain.base;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;
import java.util.ListIterator;

public abstract class BaseListView<T> extends ListView<T> {

    public BaseListView(String id) {
        super(id);
    }

    public BaseListView(String id, IModel<? extends List<? extends T>> model) {
        super(id, model);
    }

    public BaseListView(String id, List<? extends T> list) {
        super(id, list);
    }

    @Override
    protected IModel<T> getListItemModel(IModel<? extends List<T>> listViewModel, int index) {
        return new HashCodeModel<T>(this, index);
    }

    private static class HashCodeModel<T> implements IModel<T> {

        private final BaseListView<T> listView;
        private final int hashCode;

        public HashCodeModel(BaseListView<T> listView, int index) {
            this.listView = listView;
            T item = listView.getModelObject().get(index);
            hashCode = item.hashCode();
        }

        @Override
        public T getObject() {
            return listView.getModelObject().stream()
                    .filter(i -> hashCode == i.hashCode()).findFirst()
                    .orElseThrow(() -> new NoModelItemFoundException(listView, hashCode));
        }

        @Override
        public void setObject(T item) {
            ListIterator<T> iter = listView.getModelObject().listIterator();
            T listItem = null;
            while(iter.hasNext()) {
                listItem = iter.next();
                if(item.hashCode() == listItem.hashCode()) {
                    iter.set(item);
                    return;
                }
            }
            throw new NoModelItemFoundException(listView, hashCode);
        }

        @Override
        public void detach() {
            // null-op handled by list view
        }
    }

    public static class NoModelItemFoundException extends RuntimeException {

        private final int itemId;

        public NoModelItemFoundException(Object container, int itemId) {
            super("Object(" + itemId + ") is no longer in container: " + container);
            this.itemId = itemId;
        }

        public int getItemId() {
            return itemId;
        }
    }
}

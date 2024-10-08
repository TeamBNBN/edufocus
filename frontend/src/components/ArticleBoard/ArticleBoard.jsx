import EditIcon from '/src/assets/icons/edit.svg?react';
import { Link } from 'react-router-dom';
import styles from './ArticleBoard.module.css';
import { Children } from 'react';

export default function ArticleBoard({ title, canCreate, children }) {
  return (
    <div className={styles.articleBoard}>
      <div className={styles.header}>
        <div className={styles.title}>{title}</div>
        {canCreate && (
          <Link
            type="button"
            className={styles.writeButton}
            to="write"
          >
            <EditIcon className={styles.icon} />
            <span>글쓰기</span>
          </Link>
        )}
      </div>
      <div className={styles.article}>
        {Children.toArray(children).length ? children : <div className={styles.empty}>표시할 내용이 없습니다.</div>}
      </div>
    </div>
  );
}

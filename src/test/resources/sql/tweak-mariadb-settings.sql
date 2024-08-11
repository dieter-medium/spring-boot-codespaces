SET GLOBAL sync_binlog = 0;
SET GLOBAL innodb_doublewrite = 0;
SET GLOBAL innodb_flush_log_at_trx_commit = 2;
SET GLOBAL innodb_purge_threads = 0;
SET GLOBAL innodb_buffer_pool_size = 268435456; -- 256M in bytes
SET GLOBAL innodb_log_file_size = 50331648; -- 48M in bytes